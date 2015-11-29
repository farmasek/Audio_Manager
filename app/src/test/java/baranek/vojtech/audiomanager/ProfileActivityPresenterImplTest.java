package baranek.vojtech.audiomanager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import baranek.vojtech.audiomanager.model.TimerProfile;
import baranek.vojtech.audiomanager.profileActivity.ProfileActivityPresenter;
import baranek.vojtech.audiomanager.profileActivity.ProfileActivityPresenterImpl;
import baranek.vojtech.audiomanager.profileActivity.ProfileActivityView;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Created by Farmas on 25.11.2015.
 */
public class ProfileActivityPresenterImplTest {

    @Mock
    ProfileActivityView mProfileActivityView;

    private ProfileActivityPresenter mProfileActivityPresenter;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mProfileActivityPresenter = new ProfileActivityPresenterImpl(mProfileActivityView);
    }

    @Test
    public void testSetEndStatus() throws Exception {
        TimerProfile timerProfile = new TimerProfile();
        timerProfile.setCasDoKonce(0);
        mProfileActivityPresenter.setEndStatus(timerProfile, true);

        verify(mProfileActivityView).setCheckboxStatus(true);

    }
}