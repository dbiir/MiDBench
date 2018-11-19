package cn.ruc.edu.basecore;

public class SortedTuple<SortAttributeContent, SortedDataType, SortedType> {
    public SortAttributeContent sortAttributeContent;
    public SortedDataType sortedDataType;
    public SortedType sortedType;

    public SortedTuple(SortAttributeContent s, SortedDataType d,
                       SortedType t){
        this.sortAttributeContent = s;
        this.sortedDataType = d;
        this.sortedType = t;
    }

    public SortAttributeContent getSortAttributeContent(){
        return this.sortAttributeContent;
    }

    public SortedDataType getSortedDataType(){
        return this.sortedDataType;
    }

    public SortedType getSortedType(){
        return this.sortedType;
    }

    public String toString(){
        return this.sortAttributeContent + "-" + this.sortedDataType + "-" +this.sortedType;
    }
}
