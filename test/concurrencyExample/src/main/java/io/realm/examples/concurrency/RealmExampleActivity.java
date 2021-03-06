/*
 * Copyright 2014 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.realm.examples.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.examples.service.model.Cat;
import io.realm.examples.service.model.Dog;
import io.realm.examples.service.model.Person;

public class RealmExampleActivity extends Activity implements View.OnClickListener {

    public static final String TAG = RealmExampleActivity.class.getName();

    private Realm realm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm_example);

        findViewById(R.id.add_record_button).setOnClickListener(this);

        realm = Realm.getInstance(getFilesDir());
        initDb();
    }

    @Override
    public void onResume() {
        super.onResume();

        start();
    }

    @Override
    public void onStop() {
        super.onStop();

        stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reload) {
            restart();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        String personName = ((TextView) findViewById(R.id.name)).getText().toString();
        String personAge = ((TextView) findViewById(R.id.age)).getText().toString();
        String petName = ((TextView) findViewById(R.id.pets_name)).getText().toString();

        Integer parseAge = 0;
        try {
            parseAge = Integer.parseInt(personAge);
        } catch (NumberFormatException e) {

        }

        realm.beginTransaction();
        Person person = realm.createObject(Person.class);
        person.setName(personName);
        person.setAge(parseAge);

//        int checkedId = ((RadioGroup) findViewById(R.id.petType)).getCheckedRadioButtonId();
//        if (checkedId == R.id.hasCat) {
//            Cat cat = realm.createObject(Cat.class);
//            cat.setName(petName);
//            RealmList<Cat> cats = person.getCats();
//            cats.add(cat);
//        } else if (checkedId == R.id.hasDog) {
//            Dog dog = realm.createObject(Dog.class);
//            dog.setName(petName);
//            person.setDog(dog);
//        }

        realm.commitTransaction();

    }

    //This is just to create the tables in Realm so that subsequent R/W operations do not fail
    private void initDb() {
        realm.beginTransaction();
        Person person = realm.createObject(Person.class);
        person.setName("Human Being");
        person.setAge(32);
//        Dog dog = realm.createObject(Dog.class);
//        dog.setName("Fido");
        realm.commitTransaction();
    }

    private void start() {
        Intent serviceIntent = new Intent(this, BgService.class);
        serviceIntent.putExtra(BgService.REALM_FILE_EXTRA, getFilesDir());
        Log.d(TAG, "Starting service...");
        this.startService(serviceIntent);
    }

    private void stop() {
        Intent serviceIntent = new Intent(this, BgService.class);
        Log.d(TAG, "Stopping service...");
        this.stopService(serviceIntent);
    }

    private void restart() {
        stop();
        start();
    }
}
