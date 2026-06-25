package app.dtos;

public record CookieContentDto (
    String userId,
    String companyId
) {
    public CookieContentDto(String userId, String companyId){
        this.userId = userId;
        this.companyId = companyId;
    }
}