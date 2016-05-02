package com.gw.util;

import com.gw.api.Constants;
import com.gw.api.GankCategory;
import com.gw.entity.GankEntity;
import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by gongwen on 2016/5/1.
 */
public class DbUtil {
    public static boolean findById(String id) {
        return SugarRecord.find(GankEntity.class, "_id=?", id).size() > 0;
    }

    public static List<GankEntity> findByType(GankCategory type,String limit) {
        return SugarRecord.find(GankEntity.class, "type=?", new String[]{type.toString()}, "", "published_at DESC",limit);
    }

    public static long save(GankEntity gankEntity) {
        return gankEntity.save();
    }

    public static void save(List<GankEntity> gankEntities) {
        SugarRecord.saveInTx();
    }


}
