package com.africasTalking.elmer.core
package util

import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor.{ Actor, ActorRefFactory, ActorLogging, ActorRef, Props }

import io.atlabs._

import com.africasTalking._

import atlas.core.util.AtlasCoreServiceT

trait ElmerCoreServiceT extends AtlasCoreServiceT