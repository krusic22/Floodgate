/*
 * Copyright (c) 2019-2020 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Floodgate
 */

package org.geysermc.floodgate.api;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.geysermc.floodgate.util.BedrockData;
import org.geysermc.floodgate.util.EncryptionUtil;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ProxyFloodgateApi extends SimpleFloodgateApi {
    private final Map<UUID, String> encryptedData = new HashMap<>();
    private PrivateKey key;

    @Inject
    public void init(@Named("floodgateKey") PrivateKey key) {
        this.key = key;
    }

    public String getEncryptedData(UUID uuid) {
        return encryptedData.get(uuid);
    }

    public void addEncryptedData(UUID uuid, String encryptedData) {
        this.encryptedData.put(uuid, encryptedData); // just override already existing data I guess
    }

    public void removeEncryptedData(UUID uuid) {
        encryptedData.remove(uuid);
    }

    public void updateEncryptedData(UUID uuid, BedrockData bedrockData) {
        //todo move away from public/private key system
        try {
            String data = EncryptionUtil.encryptBedrockData(key, bedrockData);
            addEncryptedData(uuid, data);
        } catch (Exception exception) {
//            throw new IllegalStateException("We failed to update the BedrockData, " +
//                    "but we can't continue without the updated version!", exception);
            System.out.println("We failed to update the BedrockData, " +
                    "but we can't continue without the updated version!");
            exception.printStackTrace();
        }
    }
}
