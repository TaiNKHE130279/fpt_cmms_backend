package com.cmms.demo.domain;

import com.cmms.demo.dto.ApiDTO;
import com.cmms.demo.serviceImpl.AuthorityServiceImpl;
import com.cmms.demo.serviceImpl.RoleServiceImpl;
import com.cmms.demo.serviceImpl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EndpointListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndpointListener.class);

    @Autowired
    AuthorityServiceImpl service;
    @Autowired
    UserServiceImpl userService;

    @Autowired
    RoleServiceImpl roleService;

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        createApi(applicationContext);
        createApiItem(applicationContext);
        List<ApiItem> items = service.getAllApiItem();
        createRoleApi(items);
        creatUserApi(items);
    }

    private void creatUserApi(List<ApiItem> items) {
        List<UserPOJO> users = userService.findAll();
        List<UserApi> userApis = service.getAllUserApi();

        for (UserPOJO pojo : users) {
            for (ApiItem item : items) {
                UserApi userApi = new UserApi();
                userApi.setApiItem(item);
                userApi.setUser(pojo);

                UserApi x = userApis.stream().filter(userApi1 -> userApi1.getApiItem().getApi().getUrl().equals(userApi.getApiItem().getApi().getUrl()) && userApi1.getApiItem().getUrl().equals(userApi.getApiItem().getUrl()) && userApi1.getUser().getAccount().equals(userApi.getUser().getAccount())).findFirst().orElse(null);
                if (x == null) {
                    service.saveUserApi(userApi);
                }
            }
        }
    }


    private void createRoleApi(List<ApiItem> items) {
        List<RolePOJO> roles = roleService.findAll();
        List<RoleApi> roleApis = service.getAllRoleApi();

        for (RolePOJO role : roles) {
            for (ApiItem item : items) {
                RoleApi roleApi = new RoleApi();
                roleApi.setApiItem(item);
                roleApi.setRole(role);
                RoleApi x = roleApis.stream().filter(userApi1-> userApi1.getApiItem().getApi().getUrl().equals(roleApi.getApiItem().getApi().getUrl())&& userApi1.getApiItem().getUrl().equals(roleApi.getApiItem().getUrl()) && userApi1.getRole().getRole_name().equals(roleApi.getRole().getRole_name())).findFirst().orElse(null);
                if (x == null) {
                    service.saveRoleApi(roleApi);
                }
            }
        }

    }

    private void createApi(ApplicationContext context) {
        List<String> apis_url = service.getAll().stream().map(api -> api.getUrl()).collect(Collectors.toList());
        List<String> api_text = new ArrayList<>();
        context.getBean(RequestMappingHandlerMapping.class)
                .getHandlerMethods().forEach((key, value) -> {
                    String url = replaceText(key.getActivePatternsCondition().toString().replace("[/", "").replace("]", ""));
                    if (key.getName() != null) {
                        if (url.contains("api")) {
                            String[] names = processName(key.getName());
                            String url_api = getApiFromUrl(url);
                            if (!apis_url.contains(url_api)) {
                                if (!api_text.contains(url_api)) {
                                    api_text.add(url_api);
                                    ApiDTO dto = new ApiDTO();
                                    dto.setName(names[0]);
                                    dto.setUrl(url_api);
                                    service.addNewApi(dto);
                                }
                            }
                        }
                    }
                }
        );
    }

    private void createApiItem(ApplicationContext context) {
        List<Api> apis = service.allApi();
        List<ApiItem> itemsformDB = service.getAllApiItem();
        List<ApiItem> dtos = new ArrayList<>();
        context.getBean(RequestMappingHandlerMapping.class)
                .getHandlerMethods().forEach((key, value) -> {
                    String url = replaceText(key.getActivePatternsCondition().toString().replace("[/", "").replace("]", ""));
                    String api_url = getApiFromUrl(url);
                    String apiItem_url = getActionFromUrl(url);
                    String method = key.getMethodsCondition().toString().replace("[", "").replace("]", "");
                    String name = "";
                    if (key.getName() != null) {
                        String[] names = processName(key.getName());
                        if (key.getName().contains("#")) {
                            name = names[1];
                        }
                    }
                    Api api = getApiByUrl(apis, api_url);
                    if (api != null) {
                        if (name != null && !name.isEmpty()) {
                            ApiItem item = new ApiItem();
                            item.setName(name);
                            item.setDescription("nothing");
                            item.setMethod(method);
                            item.setUrl(apiItem_url);
                            item.setApi(api);
                            if (!isExist(item, itemsformDB)) {
                                dtos.add(item);
                            }
                        }
                    }
                }
        );
        //dtos.forEach(apiItem -> LOGGER.info("{} {} {} {}", apiItem.getUrl(), apiItem.getMethod(), apiItem.getName(), apiItem.getApi().getUrl()));
        dtos.forEach(apiItem -> service.saveApiItem(apiItem));
    }

    private boolean isExist(ApiItem apiItem, List<ApiItem> apiItems) {
        ApiItem api = apiItems.stream().filter(apiItem1 -> apiItem1.getUrl().equals(apiItem.getUrl()) && apiItem1.getApi().getUrl().equals(apiItem.getApi().getUrl()) && apiItem1.getMethod().equals(apiItem.getMethod())).findFirst().orElse(null);
        return api != null ? true : false;
    }

    private Api getApiByUrl(List<Api> apis, String url) {
        return apis.stream().filter(api -> api.getUrl().equals(url)).findFirst().orElse(null);
    }

    private String[] processName(String name) {
        String[] x = name.split("\\#");
        return x;
    }

    private String replaceText(String text) {
        if (text.contains("/{")) {

            String s = text.replaceAll("(\\/\\{)(.*?)(\\})", "");
            return s;
        }
        return text;
    }

    private String getActionFromUrl(String url) {
        String temp[] = url.split("/");
        String value = "";
        int count = 0;
        for (String x : temp) {
            if (count > 2) {
                value += "/" + x;
            }
            count++;
        }
        return value;
    }

    private String getApiFromUrl(String input) {
        String temp[] = input.split("/");
        String value = "";
        int count = 0;
        for (String x : temp) {
            value += "/" + x;
            if (count == 2) {
                break;
            }
            count++;
        }
        return value.substring(1);
    }


}
