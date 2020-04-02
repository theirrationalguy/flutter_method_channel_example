package com.example.flutter_method_channel_example;

import android.app.Activity;
import android.content.Intent;
import android.provider.CalendarContract;

import androidx.annotation.NonNull;

import com.example.flutter_method_channel_example.calendar.Methods;

import java.util.Calendar;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
  final static String CALENDAR_CHANNEL = "calendar_channel";

  private MethodChannel.Result result;

  @Override
  public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
    GeneratedPluginRegistrant.registerWith(flutterEngine);

    new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CALENDAR_CHANNEL)
            .setMethodCallHandler((MethodCall call, MethodChannel.Result result) -> {
              switch (call.method) {
                case Methods.AddEvent:
                  this.result = result;
                  addCalendarEvent("My Event!", "This is my first event I'm adding", "The New York Public Library");
                  result.success(true);
                  break;
                default:
                  result.notImplemented();
              }
            });
  }

  private void addCalendarEvent(String title, String description, String location) {
    Intent intent = new Intent(Intent.ACTION_INSERT);
    intent.setType("vnd.android.cursor.item/event");

    Calendar calendar = Calendar.getInstance();
    long startTime = calendar.getTimeInMillis();
    long endTime = calendar.getTimeInMillis() + 60 * 60 * 1000;

    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
    intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);
    intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
    intent.putExtra(CalendarContract.Events.TITLE, title);
    intent.putExtra(CalendarContract.Events.DESCRIPTION, description);
    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location);
    intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");

    startActivity(intent);
  }
}
