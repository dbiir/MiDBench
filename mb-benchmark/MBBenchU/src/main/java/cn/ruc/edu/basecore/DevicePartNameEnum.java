package cn.ruc.edu.basecore;

public enum DevicePartNameEnum {
    BLADELENGTH(1), BLADEWIDTH(0), TOWERHEIGHT(2);

    int value;

    DevicePartNameEnum(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}
