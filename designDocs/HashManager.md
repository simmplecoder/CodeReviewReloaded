## HashManager

The job of hash manager is to know what IP is logged in under what username, and automatically sign them out if they are inactive for too long. 

## LoginState

The manager enumerates possible login states when asked with combination of username and hash:

 - `NO_HASH`: used when user doesn't have entry in the manager
 - `HASH_MISMATCH`: used when user does have an entry, but provided hash differs from the one stored in the manager
 - `CORRECT_HASH`: used when user does have an entry in the manager and the provided hash matches the one stored in the manager

## Interface

- `byte[] generateHash(String username, byte[] ip)`:
    
    Should be called when the user is successfully logged in, e.g. passes check by `PasswordKeeper`. New hash is generated for the user and IP combination.
    
- `void prolongHash(String username, byte[] ip, byte[] primitiveHash)`:

    Should be used to prolong the session of the particular user with IP on any activity shown by the user.

 - `LoginState isLoggedIn(String username, byte[] ip, byte\[] primitiveHash)`:

    Tells the current login state of the particular username with IP. 

 - `int expirationPeriodMinutes()`:

    Tells the amount of time the hash is stored after user's last activity. The same for all users.

- `void shutdown()`:

    The manager will probably use timers which are not garbage collected, so this function should immediately cancel all timers to improve shutdown process of the backend.

## OneToOneHashManager

**Currently used implementation of HashManager.**

---

### LoginInstance

Basically a pair of username and IP.

---

Basically keeps a map of login instances to hashes, and vice versa, to guarantee one to one property. For timers it uses  scheduled executor using which the manager can use only two threads for all of the timers. It also stores the current state of the random hash generator.


