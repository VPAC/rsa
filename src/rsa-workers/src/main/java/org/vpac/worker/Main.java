package org.vpac.worker;

import akka.actor.*;
import akka.cluster.Cluster;
import akka.contrib.pattern.ClusterClient;
import akka.contrib.pattern.ClusterSingletonManager;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.vpac.ndg.geometry.TileManager;
import org.vpac.ndg.query.io.DatasetProvider;
import org.vpac.ndg.query.io.ProviderRegistry;
import org.vpac.ndg.storage.util.TimeSliceUtil;

public class Main {
	/**
	 * The application context should only be initialised once EVER - otherwise
	 * you get resource leaks (e.g. extra open sockets) when using something
	 * like Nailgun. The use of the enum here ensures this. The context acquired
	 * here is passed automatically to {@link AppContext} in the Storage
	 * Manager for use by other parts of the RSA.
	 */
	TimeSliceUtil timeSliceUtil;
	TileManager tileManager;
	
	private static enum AppContextSingleton {
		INSTANCE;

		public ApplicationContext appContext;

		private AppContextSingleton() {
			appContext = new ClassPathXmlApplicationContext(
					new String[] {"spring/config/BeanLocations.xml"});
		}
	}

	public void initBeans() {
		ApplicationContext appContext = AppContextSingleton.INSTANCE.appContext;

		DatasetProvider dataProvider = (DatasetProvider)appContext.getBean("rsaDatasetProvider");
		ProviderRegistry.getInstance().clearProivders();
		ProviderRegistry.getInstance().addProivder(dataProvider);	
	}

	public void startService() throws InterruptedException {
		Address joinAddress = startBackend(null);
	    Thread.sleep(5000);
//	    startBackend(joinAddress, "backend");
	    startWorker(joinAddress);
	}
	
	
	
	  public static void main(String[] args) throws InterruptedException {
		Main main = new Main();
		main.initBeans();
		main.startService();
		
	//    startWorker(joinAddress);
	//    Thread.sleep(5000);
	//    startFrontend(joinAddress);
	  }
	
	  private static String systemName = "Workers";
	  private static FiniteDuration workTimeout = Duration.create(100, "seconds");
	
	  public static Address startBackend(Address joinAddress) {
	    Config conf = ConfigFactory.parseString("akka.cluster.roles=[backend]").
				withFallback(ConfigFactory.load("master"));
	    System.out.println("Config files:" + ConfigFactory.systemProperties().toString());
	    ActorSystem system = ActorSystem.create(systemName, conf);
	    Address realJoinAddress =
	      (joinAddress == null) ? Cluster.get(system).selfAddress() : joinAddress;
	    Cluster.get(system).join(realJoinAddress);
	
	    system.actorOf(ClusterSingletonManager.defaultProps(Master.props(workTimeout), "active",
	      PoisonPill.getInstance(), "backend"), "master");
	
	    return realJoinAddress;
	  }
	
	  public static void startWorker(Address contactAddress) {
	    Config conf = ConfigFactory.parseString("akka.cluster.roles=[backend]").
				withFallback(ConfigFactory.load("worker"));
	    ActorSystem system = ActorSystem.create(systemName, conf);
	    Set<ActorSelection> initialContacts = new HashSet<ActorSelection>();
	    initialContacts.add(system.actorSelection(contactAddress + "/user/receptionist"));
	    ActorRef clusterClient = system.actorOf(ClusterClient.defaultProps(initialContacts),
	      "clusterClient");
	    system.actorOf(Worker.props(clusterClient, Props.create(WorkExecutor.class)), "worker");
	  }
	
	  public static void startFrontend(Address joinAddress) {
	    ActorSystem system = ActorSystem.create(systemName);
	    Cluster.get(system).join(joinAddress);
	 //   ActorRef frontend = system.actorOf(Props.create(Frontend.class), "frontend");
	//    system.actorOf(Props.create(WorkProducer.class, frontend), "producer");
	    system.actorOf(Props.create(WorkResultConsumer.class), "consumer");
	  }
}