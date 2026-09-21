package net.foxyas.changedaddon.process;

/**
 * Which kinds of grabs are eligible for persistent grabbing (see the
 * "Persistent Grabbing" config option). Only relevant if that option is enabled.
 */
public enum GrabPersistenceMode {
    /** Only grabs where the entity is NOT doing a "safe" grab persist — i.e. the
     *  entity isn't holding its own owner as a trust/favor action. */
    THREAT,

    /** Only grabs where the entity's owner IS the grabbed player persist —
     *  e.g. a tamed/owned entity affectionately holding its own owner. */
    SAFE,

    /** Both kinds of grabs persist through logout/login. */
    BOTH
}