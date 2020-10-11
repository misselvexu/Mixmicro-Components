package xyz.vopen.mixmicro.components.enhance.schedule.quartz.repository

import xyz.vopen.mixmicro.components.enhance.schedule.quartz.MongoHelper
import org.quartz.impl.calendar.DailyCalendar
import spock.lang.Specification

import static CalendarRepository.CALENDAR_NAME
import static CalendarRepository.CALENDAR_SERIALIZED_OBJECT

class CalendarRepositoryTest extends Specification {

    def dao = new CalendarRepository(MongoHelper.getCalendarsColl())

    def setup() {
        dao.createIndex()
        MongoHelper.purgeCollections()
    }

    def 'should store given calendar'() {
        given:
        def name = 'my cal'
        def calendar = new DailyCalendar('10:15', '10:30')

        when:
        dao.store(name, calendar)
        def stored = MongoHelper.getFirst('calendars')

        then:
        stored != null
        stored.getString(CALENDAR_NAME) == name
        stored.get(CALENDAR_SERIALIZED_OBJECT) != null
    }

    def 'should return null when calendar name is null'() {
        expect:
        dao.retrieveCalendar(null) == null
    }

    def 'should return null when there is no such calendar'() {
        expect:
        dao.retrieveCalendar('aoeu') == null
    }

    def 'should retrieve stored calendar'() {
        given:
        def name = 'my cal'
        def calendar = new DailyCalendar('10:15', '10:30')

        when:
        dao.store(name, calendar)
        def stored = dao.retrieveCalendar(name)

        then:
        // This comparison is not fantastic, but better than nothing:
        calendar.toString() == stored.toString()
    }

    def 'should return calendar names'() {
        given:
        def nameFirst = 'first'
        def nameSecond = 'second'

        when:
        [nameFirst, nameSecond].forEach {
            name ->
                dao.store(
                        name as String
                        , new DailyCalendar('10:15', '10:30')
                )
        }
        def calendarNames = dao.retrieveCalendarNames()

        then:
        calendarNames.size() == 2
        calendarNames.contains(nameFirst)
        calendarNames.contains(nameSecond)
    }

}