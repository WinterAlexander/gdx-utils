package me.winter.gdx.utils.collection;

import com.badlogic.gdx.utils.IdentityMap;

import static me.winter.gdx.utils.ObjectUtil.coalesce;
import static me.winter.gdx.utils.Validation.ensureNotNull;

/**
 * {@link IdentityMap} that accepts the null key by replacing it with a non
 * null placeholder
 * <p>
 * Created on 2023-06-08.
 *
 * @author Alexander Winter
 */
public class NullAcceptingIdentityMap<K, V> extends IdentityMap<K, V> {
	private final K nullKeyPlaceholder;

	public NullAcceptingIdentityMap(K nullKeyPlaceholder) {
		super();
		ensureNotNull(nullKeyPlaceholder, "nullKeyPlaceholder");
		this.nullKeyPlaceholder = nullKeyPlaceholder;
	}

	public NullAcceptingIdentityMap(K nullKeyPlaceholder, int initialCapacity) {
		super(initialCapacity);
		ensureNotNull(nullKeyPlaceholder, "nullKeyPlaceholder");
		this.nullKeyPlaceholder = nullKeyPlaceholder;
	}

	public NullAcceptingIdentityMap(K nullKeyPlaceholder, int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		ensureNotNull(nullKeyPlaceholder, "nullKeyPlaceholder");
		this.nullKeyPlaceholder = nullKeyPlaceholder;
	}

	public NullAcceptingIdentityMap(K nullKeyPlaceholder, IdentityMap<K, V> map) {
		super(map);
		ensureNotNull(nullKeyPlaceholder, "nullKeyPlaceholder");
		this.nullKeyPlaceholder = nullKeyPlaceholder;
	}

	public NullAcceptingIdentityMap(NullAcceptingIdentityMap<K, V> map) {
		this(map.nullKeyPlaceholder, map);
	}

	@Override
	public V put(K key, V value) {
		return super.put(coalesce(key, nullKeyPlaceholder), value);
	}

	@Override
	public <T extends K> V get(T key) {
		return super.get(coalesce(key, nullKeyPlaceholder));
	}

	@Override
	public V get(K key, V defaultValue) {
		return super.get(key, defaultValue);
	}

	@Override
	public V remove(K key) {
		return super.remove(coalesce(key, nullKeyPlaceholder));
	}

	@Override
	public boolean containsKey(K key) {
		return super.containsKey(coalesce(key, nullKeyPlaceholder));
	}

	@Override
	public K findKey(Object value, boolean identity) {
		K key = super.findKey(value, identity);

		if(key == nullKeyPlaceholder)
			return null;

		return key;
	}

	public Keys<K> keys() {
		throw new UnsupportedOperationException("NullAcceptingIdentityMap doesn't support iterating over keys");
	}

	public Entries<K, V> entries() {
		throw new UnsupportedOperationException("NullAcceptingIdentityMap doesn't support iterating over entries");
	}
}
