mainApp.service('LoginSrv', function ($http, $rootScope, $cookies, $route, $q, RqstSrv,$state) {

    var loginSrv = {};

    loginSrv.prefill = function(mail) {
        credentials.email = mail;
    };

    /* Methode de demande d'authentification */
    var authenticate = function(credentials, successCallback, errorCallback) {
        RqstSrv.post("/session/login", {
            "email": credentials.email,
            "password": credentials.password
        }, function(data) {
            $rootScope.authenticated = true;
            $rootScope.authToken = data.authToken;
            $cookies.put('authToken', $rootScope.authToken);
            successCallback(data);
        }, function(message) {
            eraseSession();
            errorCallback(message);
        });
    };

    /* Méthode de login */
    loginSrv.login = function(email, password){
        var deferred = $q.defer();
        var credentials = {
            "email": email,
            "password": password
        };
        eraseSession();
        authenticate(credentials, function(data) {
            RqstSrv.get("/users/fromAuthToken/" + $rootScope.authToken, function(data) {
                $rootScope.connectedUser = data;
                $state.go('dashboard');
                deferred.resolve($rootScope.authToken);
                return deferred.promise;
            }, function(data) {
                $rootScope.connectedUser = null;
                deferred.reject();
                return deferred.promise;
            });
        }, function(message) {
            $location.path("/login");
        });
    };

    /* Méthode de logout */
    loginSrv.logout = function() {
        RqstSrv.get("/session/logout", eraseSession, eraseSession);
        $state.go('login');
    };

    function eraseSession() {
        $rootScope.connectedUser = null;
        $rootScope.authenticated = false;
        $rootScope.authToken = null;
        $cookies.remove('authToken');
    }

    return loginSrv;

});