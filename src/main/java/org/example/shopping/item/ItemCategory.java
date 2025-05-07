package org.example.shopping.item;

public enum ItemCategory {

    ELECTRONICS {
        @Override
        public boolean isEdible() {
            return false;
        }
    },
    BABY_CARE{
        @Override
        public boolean isEdible() {
            return false;
        }
    },
    FOOD{
        @Override
        public boolean isEdible() {
            return true;
        }
    },
    ALCOHOL{
        @Override
        public boolean isEdible() {
            return true;
        }
    };


    public abstract boolean isEdible();

}