## Password keeper

The job of password keeper is to persistently store usernames and passwords, and provide secure, exclusive access to them.

## Interface

- `boolean synchronized exists(String user, String password)`

    The method should check if the combination of `user` and `password` exists.

 - `boolean synchronized register(String user, String password)`

    The method should add the `user` + `password` combination to persistent (and secure, if possible) storage.

## UnencryptedPasswordKeeper

**Currently used implementation of password keeper interface.**

Basically the class is a plaintext version of the password keeper. It operates on a plaintext file.

 - `constructor(String filename)`

    The constructor should initialize the `PasswordKeeper` with the contents of the file.

### File format

The easiest format will be (in regex)

    (\.+) : (\.+)\n
e.g. sequence of non-space characters followed by `" : "` (space, colon, space), followed by another sequence of non-space characters. One can use the regex to actually retrieve the `username` and `password` by using `.group(1)` for username and `.group(2)` for password.
