package com.astaro.midmmo.client.info.classes;

import net.minecraft.resources.ResourceLocation;

public class ClassInfo {

    public String className;
    public String classDescriprtion;
    public ResourceLocation location;

    public ClassInfo(String clazz, String desc, ResourceLocation location){
        this.className = clazz;
        this.classDescriprtion = desc;
        this.location = location;
    }

}
