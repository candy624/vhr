package org.javaboy.vhr.model;

import java.io.Serializable;

/**
 * Created by candy on 2020/10/25.
 */
public class HrRole implements Serializable {

    private Integer id;

    private Integer hrid;

    private Integer rid;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHrid() {
        return hrid;
    }

    public void setHrid(Integer hrid) {
        this.hrid = hrid;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }
}
