# nCipher_Install
### Java cross platform Security World installer

A commandline script that installs the nCipher Security World client to Windows and Linux hosts. Vanilla install or upgrades to exiting software can be deployed.

Will search for existing SW ISOs on the host machine:

**Linux**

1. Mount ISO
2. Unpack ISO
3. Untar packages and bundles
4. Run install script
5. Pre-install checks for Java and Environment variable configuration.
6. Post-install checks install drivers
7. Restart service and run enquiry/nfkminfo
8. Ready for user to manually create new-world.

**Windows**

//TODO

1. Mount ISO
2. Run installer .exe. or .msi in interactive or silent mode
3. Pre-install checks for Java and Environment variable configuration.
4. Restart service and run enquiry/nfkminfo
5. Ready for user to manually create new-world.

##### Usage

```
usage: nCipher Install [-h] [-l {debug,info,warning}] [-t {install,remove}]
                        [--version VERSION] [-f FILE] [-s {Yes,No}]
 
 Installs and configures a Security World.
 
 named arguments:
   -h, --help             show this help message and exit
   -l {debug,info,warning}, --level {debug,info,warning}
                          Specify logging level. (default: info)
   -t {install,remove}, --type {install,remove}
                          Specify install type. (default: install)
   --version VERSION
   -f FILE, --file FILE   Hotfix/Security World File to install
   -s {Yes,No}, --silent {Yes,No}
                          Silent mode install (default: No)
```

##### Configuration
The script can be maintained from the JSON files, nn-Bundle.json and secWorld.json.
Bundle references the installation process to verify mandatory and optional tar files.
SW references the JDK download versions, ISO search paths and product release versions.
