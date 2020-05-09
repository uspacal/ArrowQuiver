package me.uspacel.callisquiver;

import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;


public class QRecipe {

    private Recipe r;
    public QRecipe (Recipe r){
        this.r = r;
    }

    public boolean equals(Recipe r) {
        if (this.r == r) return true;
        if (r.getClass() != this.r.getClass()) return false;
        if(r instanceof ShapedRecipe){

        }


        return false;
    }

}
