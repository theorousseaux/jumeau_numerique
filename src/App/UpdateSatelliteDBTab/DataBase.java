package src.App.UpdateSatelliteDBTab;

public class UpdateDBModel {

    // ici mettre tous les attributs dont le controller pourrait avoir besoin

    String sqlFile;

    int Nsatellites;

    public String getSqlFile ( ) {
        return sqlFile;
    }

    public void setSqlFile ( String sqlFile ) {
        this.sqlFile = sqlFile;
    }

    public int getNsatellites ( ) {
        return Nsatellites;
    }

    public void setNsatellites ( int nsatellites ) {
        Nsatellites = nsatellites;
    }
// ici que les getters et setters
}
