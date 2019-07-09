package ru.pasha.tosamara.methods;

/**
 * Base interface of object, which encapsulates call ToSamara API methods
 */
public interface Method {
    /**
     * Generate auth key for access to ToSamara API methods
     * @return generated key
     */
    String authKey();
}
