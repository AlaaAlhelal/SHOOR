package com.shoor.shoor;


import android.support.v7.util.DiffUtil;

import java.util.ArrayList;
import java.util.List;

public class DiffUtilCallback  extends DiffUtil.Callback {
        List<Doctor> newList;
        List<Doctor> oldList;

        public DiffUtilCallback(List<Doctor> newList, List<Doctor> oldList) {
            this.newList = newList;
            this.oldList = oldList;
        }

        @Override
        public int getOldListSize() {
            return oldList != null ? oldList.size() : 0 ;
        }

        @Override
        public int getNewListSize() {
            return newList != null ? newList.size() : 0 ;
        }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }


}
