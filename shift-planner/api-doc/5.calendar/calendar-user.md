baseUrl = https://descartes-api.com

# View team plan
## Get all shifts in team between two chosen dates
**GET** {baseUrl}/calendar/teamPlan/teamId/{TeamId}/StartDate/{StartDate}/EndDate/{EndDate}

**Request body:**
```json
{}
```
**Response:**
* 200 OK
* 401 Unauthorized
* 403 Forbidden

**Response body:**
```js
{
    Shifts: [
        {
            ShiftId: , 
            StartDate,
            EndDate,
            UserID,
            FirstName,
            LastName,
            Email,
            Phone,
            Note
        }
    ]
}
```
**Select:**
```sql
select 
    ShiftId,
    StartDate,
    EndDate,
    UserID,
    FirstName,
    LastName,
    Email,
    Phone,
    Note,
from Shift
    join User using (UserId)
where TeamId = parTeamId
and StartDate >= parStartDate
and EndDate <= parEndDate;
```


# View planned shifts
## Get all planned shifts assigned to user in team between two chosen dates
**GET** {baseUrl}/calendar/plannedShifts/teamId/{TeamId}/StartDate/{StartDate}/EndDate/{EndDate}

**Request body:**
```json
{}
```
**Response:**
* 200 OK
* 401 Unauthorized
* 403 Forbidden

**Response body:**
```js
{
    Shifts: [
        {
            ShiftId, 
            StartDate,
            EndDate,
            Note
        }
    ]
}
```
**Select:**
```sql
select 
    ShiftId, 
    StartDate,
    EndDate,
    Note
from Shift  
where TeamId = parTeamId
and UserId = parUserId
and StartDate >= parStartDate
and EndDate <= parEndDate;
```


# name
## api_short_desc:
**POST/PUT/GET** {baseUrl}/calendar/plannedShifts

**Request body:**
```js
{}
```
**Response:**
* 200 OK
* 401 Unauthorized
* 403 Forbidden

**Response body:**
```js
{}
```
**Select:**
```sql
```