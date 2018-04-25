package com.colin29.hotkeytrainer.util.exception;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class StackTraceAppender {

   public static void main(String[] args) {
      try {
         thrower("Oh noes!");
      } catch (Exception e) {
         appendToFile(e);
      }

      try {
         thrower("I died!");
      } catch (Exception e) {
         appendToFile(e);
      }
   }

   public static void thrower(String message) throws Exception {
      throw new RuntimeException(message);
   }

   public static void appendToFile(Throwable e) {
      try {
         FileWriter fstream = new FileWriter("exception.txt", true);
         BufferedWriter out = new BufferedWriter(fstream);
         PrintWriter pWriter = new PrintWriter(out, true);
         e.printStackTrace(pWriter);
      }
      catch (Exception ie) {
         throw new RuntimeException("Could not write Exception to file", ie);
      }
   }
}
