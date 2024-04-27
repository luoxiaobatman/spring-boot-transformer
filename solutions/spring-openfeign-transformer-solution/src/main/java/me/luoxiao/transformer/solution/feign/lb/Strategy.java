package me.luoxiao.transformer.solution.feign.lb;

import me.luoxiao.transformer.solution.feign.FeignProperties;

public enum Strategy {
    /**
     * fallback to a custom server
     *
     * @see FeignProperties#getServiceInstanceFallbackHost()
     * @see FeignProperties#getServiceInstanceFallbackPort()
     */
    CUSTOM_SERVER,

    /**
     * default strategy, will cause Service Unavailable
     */
    NOOP
}
