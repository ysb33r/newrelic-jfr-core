module jfrdaemon {
  exports com.newrelic.jfr.daemon;
  requires jdk.jfr;
  requires java.management;
  requires org.slf4j;
}
