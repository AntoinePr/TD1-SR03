//
//  TopVentesTableViewController.swift
//  GameApp
//
//  Created by Antoine P on 11/06/2017.
//  Copyright © 2017 Antoine Protard. All rights reserved.
//

import Alamofire
import Foundation
import SwiftyJSON
import UIKit

class TopVentesTableViewController: UITableViewController {

    var topSales: [TopSales] = []
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        loadTopSales()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return topSales.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "TopSalesCells", for: indexPath)
        if !self.topSales.isEmpty {
            let topSale = topSales[indexPath.row]
            cell.textLabel?.text = topSale.jeu
            cell.detailTextLabel?.text = String(topSale.nb_ventes) + " ventes"
        }
        return cell
    }

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    
    func loadTopSales() {
        self.getTopSales() { (json) -> () in
            for i in 0...(json.count-1) {
                self.topSales.append(TopSales(
                    jeu: json[i]["jeu"].string!,
                    nb_ventes: json[i]["nb_ventes"].int!
                ))
            }
            self.tableView.reloadData()
        }
    }
    
    func getTopSales(completion: @escaping (_ json: JSON)->Void)
    {
        var token = "Token "
        token += UserDefaults.standard.value(forKey: "token") as! String
        let headers: HTTPHeaders = ["AUTHORIZATION": token]
        
        Alamofire.request("http://localhost:8080/Projet_SR03/rest/top_ventes",
                          headers: headers)
            .responseJSON { response in
                
                let statusCode = response.response?.statusCode
                if (response.error as? AFError) != nil {
                    switch statusCode! {
                    case 400..<500:
                        print("Page not found.")
                        return
                    case 500..<600:
                        print("Serveur error.")
                        print(statusCode!)
                        return
                    default:
                        print("Le service est indisponible.")
                        print(statusCode!)
                        return
                    }
                } else if (response.error as? URLError) != nil {
                    print("Le service est indisponible.")
                    print(statusCode!)
                    return
                    
                } else if response.error == nil {
                    let json = JSON(response.result.value!)
                    completion(json)
                }
        }
        
        return
    }

}
