package com.marketplace.domain.userprofile;

import com.marketplace.framework.Strings;

public record DisplayName(String value) {
    public DisplayName {
        if (Strings.isNullOrEmpty(value)) {
            throw new IllegalArgumentException("display name cannot be null or empty");
        }

        // check for profanityName
//        if (hasProfanity(displayName))
//            throw new DomainExceptions.ProfanityFound(displayName);
    }

    //  public delegate bool CheckTextForProfanity(string text);
    //   }
    @Override
    public String toString() {
        return value;
    }
}
/*
/// <summary>
/// PurgoMalum is a simple, free, RESTful web service for filtering // and removing content of profanity, obscenity and other unwanted // text.
/// Check http://www.purgomalum.com
/// </summary>
public class PurgomalumClient
{
private readonly HttpClient _httpClient;
public PurgomalumClient() : this(new HttpClient()) { }
public PurgomalumClient(HttpClient httpClient) => _httpClient = httpClient;
public async Task<bool> CheckForProfanity(string text)
{
var result = await _httpClient.GetAsync( QueryHelpers.AddQueryString(
"https://www.purgomalum.com/service /containsprofanity", "text", text));
var value = await result.Content.ReadAsStringAsync();
return bool.Parse(value); }
 }
 */
