/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Abraham Krisnanda
 */
public class Tag {
    private int id_tag;
    private String tag_name;
    /**
     * @return the id_tag
     */
    public int getId_tag() {
        return id_tag;
    }

    /**
     * @param id_tag the id_tag to set
     */
    public void setId_tag(int id_tag) {
        this.id_tag = id_tag;
    }

    /**
     * @return the tag_name
     */
    public String getTag_name() {
        return tag_name;
    }

    /**
     * @param tag_name the tag_name to set
     */
    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }
    
    
    public void save () {
        
    }
    
    public void checkValidity () {
        
    }

}
