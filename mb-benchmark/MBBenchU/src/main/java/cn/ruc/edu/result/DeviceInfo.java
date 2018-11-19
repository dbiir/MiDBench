package cn.ruc.edu.result;

public class DeviceInfo {
    public String CPUName;
    public int coreNum;
    public int processNum;
    public float memVal;
    public float diskVal;
    public float netSpeed;

    public DeviceInfo(String name, String coreNum, String processNum,
                      String memVal, String diskVal, String netSpeed){
        this.CPUName = name;
        this.coreNum = Integer.parseInt(coreNum);
        this.processNum = Integer.parseInt(processNum);
        this.memVal = Float.parseFloat(memVal);
        this.diskVal = Float.parseFloat(diskVal);
        this.netSpeed = Float.parseFloat(netSpeed);
    }

    public boolean equals(DeviceInfo another){
        if(this.CPUName.equals(another.CPUName) && this.coreNum == another.coreNum && this.processNum == another.processNum &&
                this.memVal == another.memVal && this.diskVal == another.diskVal && this.netSpeed == another.netSpeed)
            return true;
        else
            return false;
    }

    public String toString(){
        return this.CPUName + "," + this.coreNum + "," + this.processNum + "," + this.memVal
                + "," + this.diskVal + "," + this.netSpeed;
    }
}
