package frc.com.goat8092.drivers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class WS2812 extends SubsystemBase{
    private static AddressableLED m_led;
    private static AddressableLEDBuffer m_ledBuffer;
    private int startPoint = 0;
    char[] morse = {
        '.','-','-','-','-',
        '-','-','-','.','.',
        '-','-','-','.','.',
        '.','-','-','-','-',
        '-','.','.','.','.','-',
        '.','-','-','-','-',
        '-','-','-','-','.',
        '.','.','.','-','-',
        '-','-','-','.','.'
    };

    public WS2812(int pwmID, int lenght){
        m_led = new AddressableLED(pwmID);
        m_ledBuffer = new AddressableLEDBuffer(lenght);
        m_led.setLength(lenght);
        m_led.start();
        MorseToBoolean(morse);
    }

    @Override
    public void periodic() {
        if(DriverStation.isAutonomousEnabled()){
            setSolidColor(255, 0, 255);
        }
        else if(DriverStation.getMatchTime() < 16 && DriverStation.getMatchTime()!=-1){
            morseToLed();
        }
        else if(DriverStation.isTeleopEnabled()){
            slide(DriverStation.getAlliance() == Alliance.Red ? new Color(255,0,0) : new Color(0,0,255),m_ledBuffer.getLength());
        }
        else{
            slide(new Color(255,0,255),m_ledBuffer.getLength());
        }
    }

    public void setSolidColor(int r, int g, int b){
        for (byte i = 0; i < m_ledBuffer.getLength(); i++) {
            m_ledBuffer.setRGB(i, r, g, b);
        }
        m_led.setData(m_ledBuffer);
    }

    public void turnOff(){
        setSolidColor(0, 0, 0);
    }

    private boolean state = true;
    public void slide(Color color, int size){
        for (int i = 0; i < m_ledBuffer.getLength(); i++){
            if(i <= startPoint && i >= startPoint-size) m_ledBuffer.setLED(i, color);
            else m_ledBuffer.setRGB(i, 0, 0, 0);
        }
        m_led.setData(m_ledBuffer);
        startPoint%=size*2;
        startPoint++;
    }

    
    
    int i = 0;
    public void morseToLed(){
        if(i%5==0){
            if(morseLedOutput.get(i/5)) setSolidColor(255, 0, 255);
            else turnOff();
        }
        if(i/5 < morseLedOutput.size()-1) i++;
    }

    
    List<Boolean> morseLedOutput = new ArrayList<Boolean>();
    public void MorseToBoolean(char[] morse){
        morseLedOutput.clear();
        for (char c : morse) {
            morseLedOutput.addAll(c == '.' ? Arrays.asList(true,false) : Arrays.asList(true,true,true,false));
        }
    }

}