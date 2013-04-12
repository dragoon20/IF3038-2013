/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Timestamp;

/**
 *
 * @author Abraham Krisnanda
 */
public class Task extends DBSimpleRecord
{
    private int id_task;
    private String nama_task;
    private boolean status;
    private Timestamp deadline;
    private int id_kategori;
    private int id_user;


	@Override
	protected String GetClassName() 
	{
		return "models.Task";
	}

	@Override
	protected String GetTableName() 
	{
		return "task";
	}
    
    /**
     * @return the id_task
     */
    public int getId_task() {
        return id_task;
    }

    /**
     * @param id_task the id_task to set
     */
    public void setId_task(int id_task) {
        this.id_task = id_task;
    }

    /**
     * @return the nama_task
     */
    public String getNama_task() {
        return nama_task;
    }

    /**
     * @param nama_task the nama_task to set
     */
    public void setNama_task(String nama_task) {
        this.nama_task = nama_task;
    }

    /**
     * @return the status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * @return the deadline
     */
    public Timestamp getDeadline() {
        return deadline;
    }

    /**
     * @param deadline the deadline to set
     */
    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    /**
     * @return the id_kategori
     */
    public int getId_kategori() {
        return id_kategori;
    }

    /**
     * @param id_kategori the id_kategori to set
     */
    public void setId_kategori(int id_kategori) {
        this.id_kategori = id_kategori;
    }

    /**
     * @return the id_user
     */
    public int getId_user() {
        return id_user;
    }

    /**
     * @param id_user the id_user to set
     */
    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
    
    private boolean save() 
    {
        return true;
    }
    
    private boolean checkValidity() 
    {
        return true;
    }
}
