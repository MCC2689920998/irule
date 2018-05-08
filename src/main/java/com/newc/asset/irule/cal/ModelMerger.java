package com.newc.asset.irule.cal;

import com.newc.asset.iframe.entity.Model;
import com.newc.asset.irule.def.Executable;
import com.newc.asset.irule.entity.Conclusion;

/**
 * Created by paul on 2018/5/8.
 */
public class ModelMerger implements Executable<Model, Object, Model> {
    private Conclusion conclusion = null;
    public ModelMerger(Conclusion conclusion) {
        this.conclusion = conclusion;
    }

    @Override
    public Model execute(Model model, Object operation) {
        model.put(conclusion.getPath(), operation);
        return model;
    }
}
