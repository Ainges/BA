namespace processSuite.Worker.Example;

using System.Threading;
using System.Threading.Tasks;

using AtlasEngine;
using AtlasEngine.ExternalTasks;

#pragma warning disable CS0618
[ExternalTaskHandler("ExampleETW")]
#pragma warning restore CS0618
public class ExampleHandler : IExternalTaskHandler<ExamplePayload, ExampleResult>
{
    public Task<ExampleResult> HandleAsync(ExamplePayload input, ExternalTask task,
        CancellationToken cancellationToken = new ())
    {
        var result = new ExampleResult(input.ExamplePayloadInput);

        return Task.FromResult(result);
    }
}
