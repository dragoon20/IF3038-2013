package com.account;

import java.util.Date;

public class Log 
{
	private byte idtugas;
    private String namatugas;
    private boolean statustugas;
    private Date waktuperubahan;

    public Log()
    {
        idtugas = -1;
        namatugas = "";
        statustugas = false;
        waktuperubahan = new Date();
    }
    
    public Log(byte id, String nama, boolean status)
    {
        idtugas = id;
        namatugas = nama;
        statustugas = status;
        waktuperubahan = new Date();
    }
    
    public Log(byte id, String nama, boolean status, Date date)
    {
        idtugas = id;
        namatugas = nama;
        statustugas = status;
        waktuperubahan = date;
    }
    
    /**
     * @return the idtugas
     */
    public byte getIdtugas() {
        return idtugas;
    }

    /**
     * @param idtugas the idtugas to set
     */
    public void setIdtugas(byte idtugas) {
        this.idtugas = idtugas;
    }

    /**
     * @return the namatugas
     */
    public String getNamatugas() {
        return namatugas;
    }

    /**
     * @param namatugas the namatugas to set
     */
    public void setNamatugas(String namatugas) {
        this.namatugas = namatugas;
    }

    /**
     * @return the statustugas
     */
    public boolean getStatustugas() {
        return statustugas;
    }

    /**
     * @param statustugas the statustugas to set
     */
    public void setStatustugas(boolean statustugas) {
        this.statustugas = statustugas;
    }

    /**
     * @return the waktuperubahan
     */
    public Date getWaktuperubahan() {
        return waktuperubahan;
    }

    /**
     * @param waktuperubahan the waktuperubahan to set
     */
    public void setWaktuperubahan(Date waktuperubahan) {
        this.waktuperubahan = waktuperubahan;
    }
}
