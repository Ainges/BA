namespace processSuite.Worker.Example;

public class ExamplePayload
{
    public ExamplePayload(string examplePayloadInput)
    {
        this.ExamplePayloadInput = examplePayloadInput;
    }

    public string ExamplePayloadInput {get; set;}
}
