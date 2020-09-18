package com.liansu.boduowms.modules.outstock.Model;


//物料返回类
 public   class MaterialResponseModel {

    public  int Id ;
    public  String Batchno;
    public String Materialno;
    public String Materialdescen ;
    public String Materialdesc ;
    public int Stackwarehouse ;
    public int Stackhouse;
    public int Stackarea;
    public int Length;
    public int Wide ;
    public int Hight;
    public int Volume ;
    /// <summary>
    /// 净重
    /// </summary>
    public int Weight;
    /// <summary>
    /// 毛重
    /// </summary>
    public int Netweight ;
    public int Packrule ;
    public int Stackrule ;
    public int Disrule ;
    public String Unit;
    public String Unitname ;
    public String Keeperno;
    public String Keepername ;
    public int Isdangerous ;
    public int Isactivate ;
    public int Isbond ;
    public int Isquality ;
    public int Isserial ;
    public String Partno ;
    public String Maintypecode;
    public String Maintypename ;
    public String Purchasetypecode ;
    public String Purchasetypename ;
    public String Producttypecode ;
    public String Producttypename ;
    public int Qualityday ;
    public int Qualitymon ;
    public String Brand;
    public String Placearea ;
    public String Lifecycle ;
    public String Packqty ;
    public int Palletvolume ;
    public int Palletpackqty ;
    public int Packvolume ;
    public String Spec ;
    public String Storecondition ;
    public String Specialrequire ;
    public String Protectway ;
    public String Checktypecode ;
    public String Checktypename ;
    /// <summary>
    /// 理论重量
    /// </summary>
    public float Grossweight;


    public String Companycode ;


    public String Strongholdcode ;

    public String Strongholdname ;

    public String Creater;



    public String Modifyer ;
    public float OuterQty;//外箱数量

    public float getOuterQty() {
        return OuterQty;
    }

    public void setOuterQty(float outerQty) {
        OuterQty = outerQty;
    }

    public String getBatchno() {
        return Batchno;
    }

    public void setBatchno(String batchno) {
        Batchno = batchno;
    }

    public String getCompanycode() {
        return Companycode;
    }

    public void setCompanycode(String companycode) {
        Companycode = companycode;
    }

    public String getStrongholdcode() {
        return Strongholdcode;
    }

    public void setStrongholdcode(String strongholdcode) {
        Strongholdcode = strongholdcode;
    }

    public String getStrongholdname() {
        return Strongholdname;
    }

    public void setStrongholdname(String strongholdname) {
        Strongholdname = strongholdname;
    }

    public String getCreater() {
        return Creater;
    }

    public void setCreater(String creater) {
        Creater = creater;
    }

    public String getModifyer() {
        return Modifyer;
    }

    public void setModifyer(String modifyer) {
        Modifyer = modifyer;
    }

    /// <summary>
    /// 临近天数
    /// </summary>
    public float ImpDay;


    public float Watercode ;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getMaterialno() {
        return Materialno;
    }

    public void setMaterialno(String materialno) {
        Materialno = materialno;
    }

    public String getMaterialdescen() {
        return Materialdescen;
    }

    public void setMaterialdescen(String materialdescen) {
        Materialdescen = materialdescen;
    }

    public String getMaterialdesc() {
        return Materialdesc;
    }

    public void setMaterialdesc(String materialdesc) {
        Materialdesc = materialdesc;
    }

    public int getStackwarehouse() {
        return Stackwarehouse;
    }

    public void setStackwarehouse(int stackwarehouse) {
        Stackwarehouse = stackwarehouse;
    }

    public int getStackhouse() {
        return Stackhouse;
    }

    public void setStackhouse(int stackhouse) {
        Stackhouse = stackhouse;
    }

    public int getStackarea() {
        return Stackarea;
    }

    public void setStackarea(int stackarea) {
        Stackarea = stackarea;
    }

    public int getLength() {
        return Length;
    }

    public void setLength(int length) {
        Length = length;
    }

    public int getWide() {
        return Wide;
    }

    public void setWide(int wide) {
        Wide = wide;
    }

    public int getHight() {
        return Hight;
    }

    public void setHight(int hight) {
        Hight = hight;
    }

    public int getVolume() {
        return Volume;
    }

    public void setVolume(int volume) {
        Volume = volume;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public int getNetweight() {
        return Netweight;
    }

    public void setNetweight(int netweight) {
        Netweight = netweight;
    }

    public int getPackrule() {
        return Packrule;
    }

    public void setPackrule(int packrule) {
        Packrule = packrule;
    }

    public int getStackrule() {
        return Stackrule;
    }

    public void setStackrule(int stackrule) {
        Stackrule = stackrule;
    }

    public int getDisrule() {
        return Disrule;
    }

    public void setDisrule(int disrule) {
        Disrule = disrule;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getUnitname() {
        return Unitname;
    }

    public void setUnitname(String unitname) {
        Unitname = unitname;
    }

    public String getKeeperno() {
        return Keeperno;
    }

    public void setKeeperno(String keeperno) {
        Keeperno = keeperno;
    }

    public String getKeepername() {
        return Keepername;
    }

    public void setKeepername(String keepername) {
        Keepername = keepername;
    }

    public int getIsdangerous() {
        return Isdangerous;
    }

    public void setIsdangerous(int isdangerous) {
        Isdangerous = isdangerous;
    }

    public int getIsactivate() {
        return Isactivate;
    }

    public void setIsactivate(int isactivate) {
        Isactivate = isactivate;
    }

    public int getIsbond() {
        return Isbond;
    }

    public void setIsbond(int isbond) {
        Isbond = isbond;
    }

    public int getIsquality() {
        return Isquality;
    }

    public void setIsquality(int isquality) {
        Isquality = isquality;
    }

    public int getIsserial() {
        return Isserial;
    }

    public void setIsserial(int isserial) {
        Isserial = isserial;
    }

    public String getPartno() {
        return Partno;
    }

    public void setPartno(String partno) {
        Partno = partno;
    }

    public String getMaintypecode() {
        return Maintypecode;
    }

    public void setMaintypecode(String maintypecode) {
        Maintypecode = maintypecode;
    }

    public String getMaintypename() {
        return Maintypename;
    }

    public void setMaintypename(String maintypename) {
        Maintypename = maintypename;
    }

    public String getPurchasetypecode() {
        return Purchasetypecode;
    }

    public void setPurchasetypecode(String purchasetypecode) {
        Purchasetypecode = purchasetypecode;
    }

    public String getPurchasetypename() {
        return Purchasetypename;
    }

    public void setPurchasetypename(String purchasetypename) {
        Purchasetypename = purchasetypename;
    }

    public String getProducttypecode() {
        return Producttypecode;
    }

    public void setProducttypecode(String producttypecode) {
        Producttypecode = producttypecode;
    }

    public String getProducttypename() {
        return Producttypename;
    }

    public void setProducttypename(String producttypename) {
        Producttypename = producttypename;
    }

    public int getQualityday() {
        return Qualityday;
    }

    public void setQualityday(int qualityday) {
        Qualityday = qualityday;
    }

    public int getQualitymon() {
        return Qualitymon;
    }

    public void setQualitymon(int qualitymon) {
        Qualitymon = qualitymon;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getPlacearea() {
        return Placearea;
    }

    public void setPlacearea(String placearea) {
        Placearea = placearea;
    }

    public String getLifecycle() {
        return Lifecycle;
    }

    public void setLifecycle(String lifecycle) {
        Lifecycle = lifecycle;
    }

    public String getPackqty() {
        return Packqty;
    }

    public void setPackqty(String packqty) {
        Packqty = packqty;
    }

    public int getPalletvolume() {
        return Palletvolume;
    }

    public void setPalletvolume(int palletvolume) {
        Palletvolume = palletvolume;
    }

    public int getPalletpackqty() {
        return Palletpackqty;
    }

    public void setPalletpackqty(int palletpackqty) {
        Palletpackqty = palletpackqty;
    }

    public int getPackvolume() {
        return Packvolume;
    }

    public void setPackvolume(int packvolume) {
        Packvolume = packvolume;
    }

    public String getSpec() {
        return Spec;
    }

    public void setSpec(String spec) {
        Spec = spec;
    }

    public String getStorecondition() {
        return Storecondition;
    }

    public void setStorecondition(String storecondition) {
        Storecondition = storecondition;
    }

    public String getSpecialrequire() {
        return Specialrequire;
    }

    public void setSpecialrequire(String specialrequire) {
        Specialrequire = specialrequire;
    }

    public String getProtectway() {
        return Protectway;
    }

    public void setProtectway(String protectway) {
        Protectway = protectway;
    }

    public String getChecktypecode() {
        return Checktypecode;
    }

    public void setChecktypecode(String checktypecode) {
        Checktypecode = checktypecode;
    }

    public String getChecktypename() {
        return Checktypename;
    }

    public void setChecktypename(String checktypename) {
        Checktypename = checktypename;
    }

    public float getGrossweight() {
        return Grossweight;
    }

    public void setGrossweight(float grossweight) {
        Grossweight = grossweight;
    }

    public float getImpDay() {
        return ImpDay;
    }

    public void setImpDay(float impDay) {
        ImpDay = impDay;
    }

    public float getWatercode() {
        return Watercode;
    }

    public void setWatercode(float watercode) {
        Watercode = watercode;
    }
}
