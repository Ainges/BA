using AtlasEngine;
using AtlasEngine.ApiClient;

using processSuite.Worker.Example;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddSpaYarp();

// Add services to the container.
builder.Host.UseExternalTaskWorkers();
builder.Services.AddControllers();
builder.Services.Configure<ApiClientSettings>(builder.Configuration.GetSection("AtlasEngine"));
builder.Services.AddScoped<ExampleHandler>();

// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}
// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}

app.UseHttpsRedirection();

app.UseCors(x => x
    .AllowAnyOrigin()
    .AllowAnyMethod()
    .AllowAnyHeader());

app.UseStaticFiles();
app.UseRouting();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller}/{action=Index}/{id?}");

app.UseSpaYarp();

app.UseWebSockets();

app.MapFallbackToFile("index.html");

app.Run();
