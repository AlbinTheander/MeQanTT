MeQanTT
=============

This is a project showing the basics of building an MQTT client. For anyone wanting to 
use it to implement their own client, there is work to be done. The code in this project
is *not* production quality.

Some things that are lacking:

* Proper QoS handling. At the moment, only QoS 0 (At most once) is implemented. Even
  messages that require other quality of service levels (like subscribe) are
  implemented as fire-and-forget.
* Some proper error handling. If anything goes wrong now, the exception is thrown to
  the calling method. The connect()-method is especially sensitive since it any problem
  with the internet connection would throw some kind of exception.

Contributing
------------

If you start using the library and make improvements, I'd be more than happy to accept
code contributions. :-)

I might even start working on it again if I feel there is any
interest out there.

Alternatives
------------

When this library was written, the only java option was the ia92 library from IBM, which
had a license making it unusable for anyone not communicating with an IBM server. (I
normally hold IBM rather high, but this feels like greedy stupidity.)

Now, there are other alternatives, and if you're looking for a ready library, you should
probably turn your eyes that way. You can find a list at
[the MQTT software page](http://mqtt.org/software#java).

