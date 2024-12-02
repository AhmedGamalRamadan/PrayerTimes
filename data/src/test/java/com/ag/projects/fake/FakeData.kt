package com.ag.projects.fake

import com.ag.projects.domain.entity.DataEntity
import com.ag.projects.domain.model.prayer_times.Timings

const val year = 2024
const val month = 12
const val lat = 33.11
const val lng = 33.11

val timing = Timings(
    asr = "3 AM",
    dhuhr = "12 AM",
    fajr = "5 AM",
    firstThird = "10 AM",
    imsak = "3 AM",
    isha = "3 AM",
    lastThird = "3 AM",
    maghrib = "5 AM",
    midNight = "12 AM",
    sunrise = "7 AM",
    sunset = "3 AM",
)
val dataEntityList = listOf(
    DataEntity(timings = timing),
    DataEntity(timings = timing),
    DataEntity(timings = timing),
)

val dataEntityItem =DataEntity(
    timings = timing
)

const val prayerTimesSuccessResponse ="""
    {
   "code":200,
   "status":"OK",
   "data":[
      {
         "timings":{
            "Fajr":"05:26 (+05)",
            "Sunrise":"07:09 (+05)",
            "Dhuhr":"13:38 (+05)",
            "Asr":"17:09 (+05)",
            "Sunset":"20:07 (+05)",
            "Maghrib":"20:07 (+05)",
            "Isha":"21:44 (+05)",
            "Imsak":"05:16 (+05)",
            "Midnight":"01:38 (+05)",
            "Firstthird":"23:48 (+05)",
            "Lastthird":"03:29 (+05)"
         },
         "date":{
            "readable":"01 Apr 2017",
            "timestamp":"1491019261",
            "gregorian":{
               "date":"01-04-2017",
               "format":"DD-MM-YYYY",
               "day":"01",
               "weekday":{
                  "en":"Saturday"
               },
               "month":{
                  "number":4,
                  "en":"April"
               },
               "year":"2017",
               "designation":{
                  "abbreviated":"AD",
                  "expanded":"Anno Domini"
               }
            },
            "hijri":{
               "date":"04-07-1438",
               "format":"DD-MM-YYYY",
               "day":"04",
               "weekday":{
                  "en":"Al Sabt",
                  "ar":"\u0627\u0644\u0633\u0628\u062a"
               },
               "month":{
                  "number":7,
                  "en":"Rajab",
                  "ar":"\u0631\u064e\u062c\u064e\u0628"
               },
               "year":"1438",
               "designation":{
                  "abbreviated":"AH",
                  "expanded":"Anno Hegirae"
               },
               "holidays":[]
            }
         },
         "meta":{
            "latitude":51.508515,
            "longitude":51.508515,
            "timezone":"Asia\/Yekaterinburg",
            "method":{
               "id":14,
               "name":"Spiritual Administration of Muslims of Russia",
               "params":{
                  "Fajr":16,
                  "Isha":15
               },
               "location":{
                  "latitude":54.73479099999999,
                  "longitude":55.9578555
               }
            },
            "latitudeAdjustmentMethod":"ANGLE_BASED",
            "midnightMode":"STANDARD",
            "school":"STANDARD",
            "offset":{
               "Imsak":0,
               "Fajr":0,
               "Sunrise":0,
               "Dhuhr":0,
               "Asr":0,
               "Maghrib":0,
               "Sunset":0,
               "Isha":0,
               "Midnight":0
            }
         }
      },
      {
         "timings":{
            "Fajr":"05:23 (+05)",
            "Sunrise":"07:07 (+05)",
            "Dhuhr":"13:38 (+05)",
            "Asr":"17:10 (+05)",
            "Sunset":"20:09 (+05)",
            "Maghrib":"20:09 (+05)",
            "Isha":"21:46 (+05)",
            "Imsak":"05:13 (+05)",
            "Midnight":"01:38 (+05)",
            "Firstthird":"23:48 (+05)",
            "Lastthird":"03:28 (+05)"
         },
         "date":{
            "readable":"02 Apr 2017",
            "timestamp":"1491105661",
            "gregorian":{
               "date":"02-04-2017",
               "format":"DD-MM-YYYY",
               "day":"02",
               "weekday":{
                  "en":"Sunday"
               },
               "month":{
                  "number":4,
                  "en":"April"
               },
               "year":"2017",
               "designation":{
                  "abbreviated":"AD",
                  "expanded":"Anno Domini"
               }
            },
            "hijri":{
               "date":"05-07-1438",
               "format":"DD-MM-YYYY",
               "day":"05",
               "weekday":{
                  "en":"Al Ahad",
                  "ar":"\u0627\u0644\u0627\u062d\u062f"
               },
               "month":{
                  "number":7,
                  "en":"Rajab",
                  "ar":"\u0631\u064e\u062c\u064e\u0628"
               },
               "year":"1438",
               "designation":{
                  "abbreviated":"AH",
                  "expanded":"Anno Hegirae"
               },
               "holidays":[]
            }
         },
         "meta":{
            "latitude":51.508515,
            "longitude":51.508515,
            "timezone":"Asia\/Yekaterinburg",
            "method":{
               "id":14,
               "name":"Spiritual Administration of Muslims of Russia",
               "params":{
                  "Fajr":16,
                  "Isha":15
               },
               "location":{
                  "latitude":54.73479099999999,
                  "longitude":55.9578555
               }
            },
            "latitudeAdjustmentMethod":"ANGLE_BASED",
            "midnightMode":"STANDARD",
            "school":"STANDARD",
            "offset":{
               "Imsak":0,
               "Fajr":0,
               "Sunrise":0,
               "Dhuhr":0,
               "Asr":0,
               "Maghrib":0,
               "Sunset":0,
               "Isha":0,
               "Midnight":0
            }
         }
      }
      ]
      }
"""

const val prayerTimesFailResponse="""
{
    "code": 400,
    "status": "BAD_REQUEST",
    "data": "The geographical coordinates (latitude and longitude) that you specified or we were able to calculate from the address or city are invalid."
}
"""
