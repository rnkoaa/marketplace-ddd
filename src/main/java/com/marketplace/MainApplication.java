package com.marketplace;

import com.marketplace.config.ApplicationConfig;
import com.marketplace.config.ConfigLoader;
import com.marketplace.context.ApplicationContext;
import com.marketplace.context.DaggerApplicationContext;
import com.marketplace.eventstore.jdbc.flyway.FlywayMigration;
import com.marketplace.server.SparkServer;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainApplication {

    AtomicBoolean enabled = new AtomicBoolean(true);

    public static void main(String[] args) throws InterruptedException, IOException {
        new MainApplication().start();
    }

    public void start() throws IOException {
        ApplicationConfig config = ConfigLoader.loadClasspathResource("application.yml", ApplicationConfig.class);
        ApplicationContext context = DaggerApplicationContext.
            builder()
            .config(config)
            .build();

        String dbConnectionUrl = config.getDb().getUrl();

        FlywayMigration.migrate(dbConnectionUrl);
        SparkServer server = context.getServer();
        server.run();
    }

    public void run() throws InterruptedException {
        var app = new MainApplication();
        new Thread(() -> {
            try {
                app.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(app::stop).start();
    }

    private void stop() {
        System.out.println("got stop signal, stopping...");
        enabled.compareAndSet(true, false);
    }

    void consume() throws InterruptedException {
        while (enabled.get()) {
            System.out.println("Hello, World");
            TimeUnit.MILLISECONDS.sleep(200);
        }
    }
}

// publishedClassifiedAds
// publicClassifiedAds
// byOwner

// Read
// ClassifiedAdDetails
//  public Guid ClassifiedAdId { get; set; }
//               public string Title { get; set; }
//               public decimal Price { get; set; }
//               public string CurrencyCode { get; set; }
//               public string Description { get; set; }
//    public string SellersDisplayName { get; set; }
//    public string[] PhotoUrls { get; set; }

// public class ClassifiedAdListItem{
//public Guid ClassifiedAdId { get; set; }
//public string Title { get; set; }
//public decimal Price { get; set; }
//public string CurrencyCode { get; set; }
//public string PhotoUrl { get; set; }

// Task<IEnumerable<ClassifiedAdListItem>> Query(GetPublishedClassifiedAds query);
// Task<ClassifiedAdDetails> Query(GetPublicClassifiedAd query);
// Task<IEnumerable<ClassifiedAdListItem>> Query(GetOwnersClassifiedAds query);

// public class GetPublishedClassifiedAds
//           {
//               public int Page { get; set; }
//               public int PageSize { get; set; }
//           }
//           public class GetOwnersClassifiedAd
//           {
//               public Guid OwnerId { get; set; }
//               public int Page { get; set; }
//               public int PageSize { get; set; }
//}
//           public class GetPublicClassifiedAd
//           {
//               public Guid ClassifiedAdId { get; set; }
//           }
//

// [HttpGet]
//[Route("list")]
//public Task<IActionResult> Get(QueryModels.GetPublishedClassifiedAds request) {
//}
//[HttpGet]
//[Route("myads")]
//public Task<IActionResult> Get(QueryModels.GetOwnersClassifiedAd request) {
//}
//[HttpGet]
//[ProducesResponseType((int) HttpStatusCode.OK)] [ProducesResponseType((int) HttpStatusCode.NotFound)] public Task<IActionResult> Get(QueryModels.GetPublicClassifiedAd request)
//{
//}

// this IAsyncDocumentSession session, QueryModels.GetPublishedClassifiedAds query) => session.Query<Domain.ClassifiedAd.ClassifiedAd>()
//.Where(x => x.State == ClassifiedAdState.Active) .Select(x => new PublicClassifiedAdListItem
//ClassifiedAdId = x.Id.Value,
//Price = x.Price.Amount,
//Title = x.Title.Value,
//CurrencyCode = x.Price.Currency.CurrencyCode
//})
//.Skip(query.Page * query.PageSize) .Take(query.PageSize) .ToListAsyn