mainApp.service('RqstSrv', ['$http', '$rootScope', '$q', function($http, $rootScope, $q) {
    var rqstService = {};
    var baseConf = {
        "method": "GET",
        "path": "/api_Web_exploded/rest",
        "data": null,
        "isDownload": false,
        "isUpload": false,
        "originalFileName": null,
        "isBackgroundTask": false,
        "successCallBack": function toto() {},
        "errorCallBack": function toto() {}
    };

    var call = function(conf) {
        var method = conf.method || baseConf.method;
        var path = conf.path || baseConf.path;
        var data = conf.data || baseConf.data;
        var isDownload = conf.isDownload || baseConf.isDownload;
        var isUpload = conf.isUpload || baseConf.isUpload;
        var originalFileName = conf.originalFileName || baseConf.originalFileName;
        var isBackgroundTask = conf.isBackgroundTask || baseConf.isBackgroundTask;
        var successCallBack = conf.successCallBack || baseConf.successCallBack;
        var errorCallBack = conf.errorCallBack || baseConf.errorCallBack;

        if (isBackgroundTask !== true) {
            $rootScope.currentCalls++;
            $rootScope.totalCalls++;
        }
        var config = {
            method: method,
            url: (path),
            cache: false,
            crossDomain: true,
            headers: {
                'Content-Type': "application/json"
            },
            data: data
        };

        // config.responseType = isDownload ? "blob" : config.responseType;

        if (isUpload) {
            if (!originalFileName) {
                return;
            }
            config.headers['Content-Type'] = undefined;
            config.transformRequest = angular.identity;
            config.headers.fileName = originalFileName;
        }
        if ($rootScope.authToken) {
            config.headers.authToken = $rootScope.authToken;
        }

        $http(config).then(function APICallSucceed(response) {
            if (isDownload !== true) {
                if (successCallBack) {
                    convertDateStringsToDates(response.data);
                    successCallBack(response.data);
                }
            } else {
                try {
                    if (!response || !response.data || !response.data.path) {
                        throw ("Le serveur n'a pas renvoyé une réponse valide");
                    }
                    window.open("/download?rq=" + response.data.path);
                } catch (e) {
                    if (errorCallBack) {
                        errorCallBack(e);
                        console.log(e);
                    }
                }
            }
        }, function APICallError(response) {
            var message = (response.status === 500) ? response.data.message : getDebugMessage(response.status, method, path);
            if (errorCallBack) {
                errorCallBack(message);
            }
            console.log(message);
        }).then(function() {
            if (isBackgroundTask !== true) {
                $rootScope.currentCalls--;
            }
        });
    }

    rqstService.syncGet = function(path, successCallBack, errorCallback) {
        var req = new XMLHttpRequest();
        req.open('GET', ('api' + path), false);
        req.send(null);
        if (req.status == 200) {
            var response
            try {
                response = JSON.parse(req.response);
            } catch (e) {
                response = undefined;
            }
            if (successCallBack != null) {
                convertDateStringsToDates(response);
                successCallBack(response);
            }
        } else {
            try {
                var response = JSON.parse(req.response);
            } catch (e) {

            }

            if (response && response.message) {
            }
            if (errorCallback != null) {
                errorCallback(response);
            }
        }
    }

    rqstService.syncPost = function(path, data, successCallBack, errorCallback) {
        var req = new XMLHttpRequest();
        req.open('POST', ('api' + path), false);
        req.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        req.send(JSON.stringify(data));
        if (req.status == 200) {
            var response
            try {
                response = JSON.parse(req.response);
            } catch (e) {
                response = undefined;
            }
            if (successCallBack != null) {
                convertDateStringsToDates(response)
                successCallBack(response);
            }
        } else {
            try {
                var response = JSON.parse(req.response);
            } catch (e) {
            }

            if (response && response.message) {
            }
            if (errorCallback != null) {
                errorCallback(response);
            }
        }
    }

    rqstService.post = function(path, data, successCallBack, errorCallBack) {
        call({
            "method": "POST",
            "path": baseConf.path + path,
            "data": data,
            "successCallBack": successCallBack,
            "errorCallBack": errorCallBack
        });
    }

    rqstService.get = function(path, successCallBack, errorCallBack) {
        call({
            "method": "GET",
            "path": baseConf.path + path,
            "successCallBack": successCallBack,
            "errorCallBack": errorCallBack
        });
    }

    rqstService.backgroundGet = function(path, successCallBack, errorCallBack) {
        call({
            "method": "GET",
            "path": "/api" + path,
            "isBackgroundTask": true,
            "successCallBack": successCallBack,
            "errorCallBack": errorCallBack
        });
    }

    rqstService.put = function(path, data, successCallBack, errorCallBack) {
        call({
            "method": "PUT",
            "path": baseConf.path + path,
            "data": data,
            "successCallBack": successCallBack,
            "errorCallBack": errorCallBack
        });
    }

    rqstService.delete_ = function(path, data, successCallBack, errorCallBack) {
        call({
            "method": "DELETE",
            "path": "/api" + path,
            "data": data,
            "successCallBack": successCallBack,
            "errorCallBack": errorCallBack
        });
    }

    rqstService.upload = function(path, file, successCallBack, errorCallBack) {

        var formData = new FormData();
        formData.append('file', file);
        call({
            "method": "POST",
            "path": "/api" + path,
            "data": formData,
            "isUpload": true,
            "originalFileName": file.name,
            "successCallBack": successCallBack,
            "errorCallBack": errorCallBack
        });
    }

    rqstService.download = function(path, successCallBack, errorCallBack) {
        call({
            "method": "GET",
            "path": "/api" + path,
            "isDownload": true,
            "successCallBack": successCallBack,
            "errorCallBack": errorCallBack
        });
    }

    return rqstService;

    function convertDateStringsToDates(input) {

        var regexDate = /[1-2][0-9]{3}-[0-1][0-9]-[0-3][0-9] [0-2][0-9]:[0-6][0-9]:[0-6][0-9]/;
        // Match all date following the format "2016-05-16 18:21:25"

        // Ignore things that aren't objects.
        if (typeof input !== "object") return input;

        for (var key in input) {
            if (!input.hasOwnProperty(key)) continue;

            var value = input[key];
            var match;
            // Check for string properties which look like dates.
            if (typeof value === "string" && value.length == 19 && (match = value.match(regexDate))) {
                var milliseconds = Date.parse(match[0])
                if (!isNaN(milliseconds)) {
                    input[key] = new Date(milliseconds);
                }
            } else if (typeof value === "object") {
                // Recurse into object
                convertDateStringsToDates(value);
            }
        }
    }

    function getDebugMessage(statusCode, method, path) {
        if (statusCode == 400) {
            // bad request
            return ("The server cannot or will not process the request due to an apparent client error (e.g., malformed request syntax)");
        } else if (statusCode == 401) {
            // unauthorizerd
            return ("Authentication is required and has failed or has not yet been provided");
        } else if (statusCode == 403) {
            // time out
            return ("The request was a valid request, but the server is refusing to respond to it.");
        } else if (statusCode == 405) {
            // Method not allowed
            return ("A request method " + method + " is not supported for the requested resource " + path);
        } else if (statusCode == 415) {
            // unsupported media type
            return ("Media type is not supported by server or content unreadable.");
        } else if (statusCode == 408) {
            // forbidden
            return ("The server timed out waiting for the request.");
        } else if (statusCode == 501) {
            // not implemented
            return ("The server either does not recognize the request method, or it lacks the ability to fulfill the request.");
        } else if (statusCode == 503) {
            // service unavailable
            return ("The server is currently unavailable (because it is overloaded or down for maintenance). ");
        }
    }

    function handleFileDownload(response) {
        if (!response || !response.data || !response.data.path) {
            throw ("Le serveur n'a pas renvoyé une réponse valide");
        }
        call({
            "method": "POST",
            "path": "/download",
            "data": {
                "path": response.data.path
            },
            "successCallBack": function downloadSucceed(response) {
                var filename;
                var type;
                var contentDisposition = response.headers()["content-disposition"];
                filename = contentDisposition.split(";")[1].split("=\"")[1].split("\"")[0];
                type = contentDisposition.split(";")[0];
                var anchor = angular.element('<a/>');

                anchor.attr({
                    href: 'data:' + type + '; charset=utf-8,' + encodeURI(response.data),
                    target: '_blank',
                    download: filename
                })[0].click();
            },
            "errorCallBack": function downloadError(message) {
                throw ("Erreur lors du téléchargement");
            }
        });

    }
}]);