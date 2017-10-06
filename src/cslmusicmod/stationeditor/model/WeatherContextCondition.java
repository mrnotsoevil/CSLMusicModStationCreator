package cslmusicmod.stationeditor.model;

public class WeatherContextCondition extends ContextCondition {

    private IntRange temperature;
    private IntRange rain;
    private IntRange cloudy;
    private IntRange foggy;
    private IntRange rainbow;
    private IntRange northernlights;
    private boolean not;

    public WeatherContextCondition() {
        temperature = new IntRange(-100, 100);
        rain = new IntRange(0, 10);
        cloudy = new IntRange(0, 10);
        foggy = new IntRange(0, 10);
        rainbow = new IntRange(0, 10);
        northernlights = new IntRange(0, 10);
        not = false;
    }

    @Override
    public String getType() {
        return "weather";
    }

    public IntRange getTemperature() {
        return temperature;
    }

    public void setTemperature(IntRange temperature) {
        this.temperature = temperature;
    }

    public IntRange getRain() {
        return rain;
    }

    public void setRain(IntRange rain) {
        this.rain = rain;
    }

    public IntRange getCloudy() {
        return cloudy;
    }

    public void setCloudy(IntRange cloudy) {
        this.cloudy = cloudy;
    }

    public IntRange getFoggy() {
        return foggy;
    }

    public void setFoggy(IntRange foggy) {
        this.foggy = foggy;
    }

    public IntRange getRainbow() {
        return rainbow;
    }

    public void setRainbow(IntRange rainbow) {
        this.rainbow = rainbow;
    }

    public IntRange getNorthernlights() {
        return northernlights;
    }

    public void setNorthernlights(IntRange northernlights) {
        this.northernlights = northernlights;
    }

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }

    @Override
    public ValidationResult isValid() {
        return new ValidationResult(this)
                .and(temperature)
                .and(rain)
                .and(cloudy)
                .and(foggy)
                .and(rainbow)
                .and(northernlights);
    }
}
