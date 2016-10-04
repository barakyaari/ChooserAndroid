package net.cloudapp.chooser.chooser.Network.RestFramework;

import net.cloudapp.chooser.chooser.Common.SessionDetails;

public class RestClient {
    //You need to change the IP if you testing environment is not local machine
    //or you may have different from what we have here
    private static final String URL = SessionDetails.getInstance().serverAddress;

    private retrofit.RestAdapter restAdapter;
    private ServerAPI serverAPI;

    public RestClient() {

        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        serverAPI = restAdapter.create(ServerAPI.class);
    }

    public ServerAPI getService() {
        return serverAPI;
    }
}
