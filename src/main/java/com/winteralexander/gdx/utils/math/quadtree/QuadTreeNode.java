package com.winteralexander.gdx.utils.math.quadtree;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.winteralexander.gdx.utils.math.OriginUtil;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;
import static com.winteralexander.gdx.utils.math.OriginUtil.ALIGN_BOTTOM;
import static com.winteralexander.gdx.utils.math.OriginUtil.ALIGN_CENTER;
import static com.winteralexander.gdx.utils.math.OriginUtil.ALIGN_LEFT;
import static com.winteralexander.gdx.utils.math.OriginUtil.ALIGN_RIGHT;
import static com.winteralexander.gdx.utils.math.OriginUtil.ALIGN_TOP;

/**
 * Node of a {@link QuadTree}
 * <p>
 * Created on 2019-09-15.
 *
 * @author Alexander Winter
 */
public class QuadTreeNode<T extends Bounded> {
	private final QuadTree<T> quadTree;
	private final Array<T> entities;
	@SuppressWarnings("unchecked")
	private final QuadTreeNode<T>[] childNodes = new QuadTreeNode[4];
	private final Rectangle boundary = new Rectangle();

	private int level = -1;

	public QuadTreeNode(QuadTree<T> quadTree) {
		ensureNotNull(quadTree, "quadTree");

		this.quadTree = quadTree;
		entities = new Array<>(false, quadTree.maxEntities);
	}

	public void init(int level, float x, float y, float w, float h) {
		if(this.level != -1)
			clear();

		this.level = level;
		this.boundary.set(x, y, w, h);
	}

	/**
	 * Clear out all entities and null out all child nodes
	 */
	public void clear() {
		if(level == -1)
			throw new IllegalStateException("QuadTree not initialized");

		// Clear the entities
		entities.clear();

		// Clear out each child node
		for(int i = 0; i < childNodes.length; i++)
		{
			QuadTreeNode<T> currNode = childNodes[i];
			if(currNode != null)
			{
				currNode.clear();
				quadTree.pool.free(currNode);
				childNodes[i] = null;
			}
		}

		level = -1;
	}

	/**
	 * Subdivide the node into 4 child nodes
	 */
	private void subdivide() {
		if(childNodes[0] != null)
			throw new IllegalStateException("QuadTreeNode already subdivided");

		float halfW = boundary.width / 2;
		float halfH = boundary.height / 2;
		float x = boundary.x;
		float y = boundary.y;

		// Create four child node which fully divide the boundary of this node
		QuadTreeNode<T> nwNode = quadTree.pool.obtain();
		nwNode.init(level + 1, x, y + halfH, halfW, halfH);
		childNodes[0] = nwNode;

		QuadTreeNode<T> neNode = quadTree.pool.obtain();
		neNode.init(level + 1, x + halfW, y + halfH, halfW, halfH);
		childNodes[1] = neNode;

		QuadTreeNode<T> seNode = quadTree.pool.obtain();
		seNode.init(level + 1, x + halfW, y, halfW, halfH);
		childNodes[2] = seNode;

		QuadTreeNode<T> swNode = quadTree.pool.obtain();
		swNode.init(level + 1, x, y, halfW, halfH);
		childNodes[3] = swNode;
	}

	/**
	 * Determine which quadrant of the node the specified rectangle belongs too.
	 * If overlapping, it will return a list of quadrants under the form of
	 * a direction. See {@link OriginUtil} to parse these directions.
	 */
	private int getQuadrant(Rectangle rectangle) {
		float cX = boundary.x + boundary.width / 2f;
		float cY = boundary.y + boundary.height / 2f;

		// Object can completely fit within the left quadrants
		if(rectangle.x + rectangle.width < cX) {
			if(rectangle.y > cY)
				return ALIGN_LEFT | ALIGN_TOP;
			else if(rectangle.y + rectangle.height < cY)
				return ALIGN_LEFT | ALIGN_BOTTOM;

			return ALIGN_LEFT;
		}

		// Object can completely fit within the right quadrants
		else if(rectangle.x > cX) {
			if(rectangle.y > cY)
				return ALIGN_RIGHT | ALIGN_TOP;
			else if(rectangle.y + rectangle.height < cY)
				return ALIGN_RIGHT | ALIGN_BOTTOM;

			return ALIGN_RIGHT;
		}

		if(rectangle.y > cY)
			return ALIGN_TOP;
		else if(rectangle.y + rectangle.height < cY)
			return ALIGN_BOTTOM;

		// If we get here, the object can not fit completely in a child node,
		// and will be part of the parent node
		return ALIGN_CENTER;
	}

	/**
	 * Insert an entity into the appropriate node, subdividing if necessary.
	 *
	 * @param entity entity to insert
	 */
	public void insert(T entity) {
		if(level == -1)
			throw new IllegalStateException("QuadTreeNode not initialized");

		// If we have any child nodes, see if the entity could be contained
		// completely inside of one of them
		if(childNodes[0] != null)
		{
			entity.getBounds(quadTree.tmpRectangle);
			int index = indexOf(getQuadrant(quadTree.tmpRectangle));

			// If full containment is possible, recursively insert in that node.
			if(index != -1)
				childNodes[index].insert(entity);
			else
				entities.add(entity);
			return;
		}

		// Add the entity to the list of entities for the node we are in
		entities.add(entity);

		// If we've exceeded the max number of entities for this node (And have
		// more that we could subdivide), attempt to subdivide and insert further
		if(entities.size > quadTree.maxEntities && level < quadTree.maxLevels) {
			subdivide();

			int i = 0;
			while(i < entities.size)
			{
				// Move and insert what we can into the child nodes. If it can't
				// be fully contained in the child nodes, leave it at this level.
				T other = entities.get(i);
				other.getBounds(quadTree.tmpRectangle);
				int index = indexOf(getQuadrant(quadTree.tmpRectangle));

				if(index != -1)
				{
					entities.removeIndex(i);
					childNodes[index].insert(other);
				}
				else
					i++;
			}
		}
	}

	/**
	 * Return all entities that can overlap a given rectangle
	 */
	public void retrieve(Array<T> out, Rectangle rectangle) {
		if(level == -1)
			throw new IllegalStateException("QuadTreeNode not initialized");

		// If we have any child nodes, see if the entity could be contained
		// completely inside of one of them
		if(childNodes[0] != null) {
			int quadrant = getQuadrant(rectangle);
			int index = indexOf(quadrant);

			if(index != -1)
				childNodes[index].retrieve(out, rectangle); // only ones of quadrant

			else if(quadrant == ALIGN_CENTER)
				for(QuadTreeNode<T> node : childNodes)
					node.retrieve(out, rectangle); // then all

			else switch(quadrant) {
				case ALIGN_TOP:
					childNodes[0].retrieve(out, rectangle);
					childNodes[1].retrieve(out, rectangle);
					break;

				case ALIGN_BOTTOM:
					childNodes[2].retrieve(out, rectangle);
					childNodes[3].retrieve(out, rectangle);
					break;

				case ALIGN_LEFT:
					childNodes[0].retrieve(out, rectangle);
					childNodes[3].retrieve(out, rectangle);
					break;

				case ALIGN_RIGHT:
					childNodes[1].retrieve(out, rectangle);
					childNodes[2].retrieve(out, rectangle);
					break;

				default:
					throw new IllegalStateException("Unrecognized quadrant: " + quadrant);
			}
		}

		// Add all the entities of the node we are in.
		out.addAll(entities);
	}

	/**
	 * Renders the boundaries of all the quad tree nodes in postorder depth
	 * traversal fashion recursively
	 *
	 * @param shapeRenderer renderer to debug render with
	 */
	public void debugRender(ShapeRenderer shapeRenderer) {
		if(level == -1)
			throw new IllegalStateException("QuadTreeNode not initialized");

		// render each child node
		if(childNodes[0] != null)
			for(QuadTreeNode<T> currNode : childNodes)
				currNode.debugRender(shapeRenderer);

		// Render the rect
		shapeRenderer.rect(boundary.x, boundary.y, boundary.width, boundary.height);
	}

	private int indexOf(int quadrant) {
		switch(quadrant)
		{
			case ALIGN_TOP | ALIGN_LEFT:
				return 0;
			case ALIGN_TOP | ALIGN_RIGHT:
				return 1;
			case ALIGN_BOTTOM | ALIGN_RIGHT:
				return 2;
			case ALIGN_BOTTOM | ALIGN_LEFT:
				return 3;
			default:
				return -1;
		}
	}
}