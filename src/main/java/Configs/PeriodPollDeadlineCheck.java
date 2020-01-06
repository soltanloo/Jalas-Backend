package Configs;

import BusinessLogic.MeetingServices;
import DataManagers.PollDataHandler;
import ErrorClasses.DataBaseErrorException;
import ErrorClasses.RoomReservationErrorException;
import Models.Poll;

import java.util.ArrayList;

public class PeriodPollDeadlineCheck implements Runnable {
    @Override
    public void run () {
        try {
            System.out.println("Passed deadline Polls check");

            ArrayList<Poll> ongoingPassedDeadlinePolls = PollDataHandler.getOngoingPassedDeadlinePolls();
            for (Poll poll : ongoingPassedDeadlinePolls)
                PollDataHandler.unsetOngoingStatus(poll.getId());

            ArrayList<Poll> mustSetPolls = PollDataHandler.getMustSetPolls();
            for (Poll poll : mustSetPolls)
                MeetingServices.addAutoMeeting(poll);

        } catch (DataBaseErrorException | RoomReservationErrorException e) {
            e.printStackTrace();
        }
    }
}
