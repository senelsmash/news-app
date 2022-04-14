# news-app
News App for eyepax

IMPORTANT - API KEY MAY EXPIRE ON MAKING HEAVY REQUESTS SINCE KEY IS A FREE TRIAL VERSION

Application uses a room database for user registration and login functionalities, which acts as a login/register service for demo purpose,
When a user login/register to the app once they will be auto redirected to dashboard (main view) in next attempts opening the app.a

The main view consist of a bottom navigation with Dashboard, Favorite and Profile screens.

Dashboard use cases:

- User will be presented with top headlines in a horizontal list view where user can navigate for more detail on item click.
- User will be presented with news list according to category in a vertical list view where more details can be viewed on item click.
- Filter by category option is provided to user in order to filter the vertical new list.a
- A search feature is provided for advance filtering of the list.a


Search use cases:

- User will be able to search the list by keyword, auto filtering option is provided on search text change.a
- Sort by option is also provided in a bottom sheet where user can sort the list according to different type, this is presented in a chips group.a
- User can navigate inside the filtered items for more details.

Details screen use cases:

-User will be presented with more information of the news article.
-User can mark article as favorite and will be locally saved in the device which can be viewed in favorite screen.a

Favorite screen use cases:

-User can view all the favorite articles added in this view (locally stored)

Profile screen use cases:

-Display user with user information stored in data store.
