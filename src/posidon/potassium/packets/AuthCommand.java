package posidon.potassium.packets;

import java.io.Serializable;

public class AuthCommand implements Serializable {

    private static final long serialVersionUID = 1;
    public String key;
    public String[] cmd;

    public AuthCommand(String key, String[] cmd) {
        this.key = key;
        this.cmd = cmd;
    }
}
