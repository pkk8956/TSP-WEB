package core.io;

import java.io.IOException;

public class SetUpUtil {

    public static int calcNecessaryTraveler() throws IOException {
        ConfigDAO dao = new ConfigDAO();
        int[] restriction = dao.getRestrictionsArray();
        int loadCapacity = dao.getLoadCapacityForTraveler();
        int totalRestriction = 0;
        for (int i = 1; i < restriction.length; i++) {
            totalRestriction += restriction[i];
        }
        return (int) Math.ceil((double) totalRestriction / loadCapacity);
    }

}
