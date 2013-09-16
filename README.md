TC65Lib (javacint)
=======

The goal of this library is to provide a base library for existing and new programs on all the Gemalto's M2M devices:
* TC65
* TC65i / TC65i-X
* BSG5 / BSG5-X

This library is actually intended as a framework. It is necessary to obfuscate your programs to remove the unecessary
components of the library. 

The features that will be provided in this library are:
- Settings management (done)
- Watchdog management (done)
- Logging (done)
- Console (done)
- Time sync (done)
- SMS receiving and sending (done)
- Call receiving and making
- Automatic OTAP management (done)
- Communication with server: MQTT / M2MP / HTTP ? (NOT done)
