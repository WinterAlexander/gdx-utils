package com.winteralexander.gdx.utils.math.shape2d;

import com.badlogic.gdx.utils.Array;

/**
 * Utility class for shapes
 * <p>
 * Created on 2021-05-20.
 *
 * @author Alexander Winter
 */
public class ShapeUtil {
	/**
	 * Returns all overlapping pair of children shapes for given shapes, assuming some of them may be
	 * {@link MultiShape}. The result array will always be of even length with every group of 2
	 * indices representing each pairs between shapes. The shapes in the results are guaranteed not
	 * to be {@link MultiShape} as those would have been broken down into their respective childrens.
	 *
	 * @param shapeA first shape to check children to find pairs
	 * @param shapeB second shape to check children to find pairs
	 * @param result array containing the pairs.
	 */
	public static void getOverlappingChildPairs(Shape shapeA,
	                                            Shape shapeB,
	                                            Array<Shape> result) {
		if(!(shapeA instanceof MultiShape) && !(shapeB instanceof MultiShape)) {
			if(shapeA.overlaps(shapeB))
				result.add(shapeA, shapeB);
			return;
		}

		if(shapeA instanceof MultiShape) {
			for(Shape childA : ((MultiShape)shapeA).getShapes())
				getOverlappingChildPairs(childA, shapeB, result);
			return;
		}

		for(Shape childB : ((MultiShape)shapeB).getShapes())
			getOverlappingChildPairs(shapeA, childB, result);
	}
}
