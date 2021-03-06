package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.ext.Provider;

import dao.ConnexionBDD;
import dao.Secured;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	@Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
		 
		// Get the HTTP Authorization header from the request
        String authorizationHeader = 
            requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        
        // Check if the HTTP Authorization header is present and formatted correctly 
        if (authorizationHeader == null || !authorizationHeader.startsWith("Token ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }
        
     // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Token".length()).trim();
        
        try {
            // Validate the token
            validateToken(token);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).build());
        }
	}
	
    private void validateToken(String token) throws Exception {
        // Check if it was issued by the server and if it's not expired
        // Throw an Exception if the token is invalid
    	System.out.println("validateToken called");
    	try {
    		Connection connection =  ConnexionBDD.getInstance().getCnx();
			String query;
			query = "SELECT *"
					+ "FROM adherent "
					+ "WHERE token = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, token);
			ResultSet rs = ps.executeQuery();
			// Token not found in the database
			if(!rs.next())
			{
				IOException e = new IOException();
				throw e;
			}
		}
		catch(Exception e) {
			throw e;
		}
    }
}
