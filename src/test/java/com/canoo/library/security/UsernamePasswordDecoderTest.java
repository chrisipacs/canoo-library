package com.canoo.library.security;

import org.junit.Test;

import static org.junit.Assert.*;

public class UsernamePasswordDecoderTest {

    @Test
    public void decodeUserNamePassword() {
        String basicHeaderContent = "Basic dXNlcm5hbWU6cGFzc3dvcmQ=";
        String[] usernameAndPassword = UsernamePasswordDecoder.decodeUserNamePassword(basicHeaderContent);

        assertEquals(usernameAndPassword[0],"username");
        assertEquals(usernameAndPassword[1],"password");
    }

}