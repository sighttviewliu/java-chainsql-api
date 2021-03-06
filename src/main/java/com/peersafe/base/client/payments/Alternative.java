package com.peersafe.base.client.payments;

import com.peersafe.base.core.coretypes.Amount;
import com.peersafe.base.core.coretypes.PathSet;
import com.peersafe.base.core.coretypes.hash.HalfSha512;
import com.peersafe.base.core.coretypes.hash.Hash256;
import org.json.JSONObject;

public class Alternative implements Comparable<Alternative> {

    public Amount sourceAmount;
    public PathSet paths;
    public Hash256 hash;

    /**
     * Construtor.
     * @param paths Paths.
     * @param sourceAmount Source Amount.
     */
    public Alternative(PathSet paths, Amount sourceAmount) {
        this.paths = paths;
        this.sourceAmount = sourceAmount;
        this.hash = calculateHash(paths, sourceAmount);
    }

    private Hash256 calculateHash(PathSet paths, Amount sourceAmount) {
        HalfSha512 half = new HalfSha512();
        half.update(paths.toBytes());
        half.update(sourceAmount.toBytes());
        return half.finish();
    }

    /**
     * Override.
     */
    @Override
    public int compareTo(Alternative another) {
        return hash.compareTo(another.hash);
    }

    /**
     * Is direct Xrp.
     * @return return value.
     */
    public boolean directXRP() {
        return !hasPaths() && sourceAmount.isNative();
    }

    boolean hasPaths() {
        return paths.size() > 0;
    }

    /**
     * to String.
     */
    @Override
    public String toString() {
        JSONObject o = toJSON();
        return o.toString(2);
    }

    /**
     * To JSON.
     * @return JSONObject value.
     */
    public JSONObject toJSON() {
        JSONObject o = new JSONObject();
        o.put("source_amount", sourceAmount.toJSON());
        o.put("paths", paths.toJSON());
        return o;
    }
}
