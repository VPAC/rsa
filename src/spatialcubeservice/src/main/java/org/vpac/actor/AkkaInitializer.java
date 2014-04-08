package org.vpac.actor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.Props;
import akka.cluster.Cluster;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class AkkaInitializer extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		ActorCreator.createActorCreator();
	}
}
